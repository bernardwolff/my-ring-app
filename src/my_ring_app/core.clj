(ns my-ring-app.core
  (:require [auth0-clojure.api.authentication :as auth])
  (:require [ring.middleware.params :as params]))

(defn handler [request]
  (auth/set-config! ;; get these values from https://manage.auth0.com
                  {:auth0/client-id      "____"
                   :auth0/client-secret  "______"
                   :auth0/default-domain "_____.us.auth0.com"})
  (case (request :uri)
    "/login" {:status 302
              :headers {"Location" (auth/authorize-url {:auth0/response-type "code"
                                                        :auth0/scope         "openid profile"
                                                        :auth0/redirect-uri  "http://localhost:3000/callback"})}}

    ;; TODO: set the access token and the id token in the session state, then redirect to /protected
    "/callback" {:status 200
                 :headers {"Content-Type" "text/html"}
                 :body (let [exchangeTokens (auth/oauth-token
                                             {:auth0/grant-type   :auth0.grant-type/authorization-code
                                              :auth0/code (get (request :params) "code")
                                              :auth0/redirect-uri "http://localhost:3000/callback"})]
                         
                         (str "accessToken='" (get (exchangeTokens :body) :auth0/access-token) "' "
                              "idToken='" (get (exchangeTokens :body) :auth0/access-token) "'"))}

    ;; TODO: redirect to /login if the access token and id token are both null, otherwise, show the access token and id token in the response body
    "/protected" {:status 200
                 :headers {"Content-Type" "text/html"}
                 :body "Hello World from /protected"}
    "/" {:status 200
         :headers {"Content-Type" "text/html"}
         :body "Hello World at /"}
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (str "default route. uri='" (request :uri) "' type='" (type request) "'")}))

(def app (params/wrap-params handler))

;; the auth/oauth-token function returns something like this:
;; {:cached nil,
;;  :request-time 240,
;;  :repeatable? false,
;;  :protocol-version {:name "HTTP", :major 1, :minor 1},
;;  :streaming? true,
;;  :chunked? true,
;;  :cookies {"did" ...},
;;  :reason-phrase "OK",
;;  :headers {....},
;;  :orig-content-encoding nil,
;;  :status 200,
;;  :length -1,
;;  :body {
;;         :auth0/access-token "Vy_______",
;;         :auth0/id-token "eyJhbGci______.eyJn_____________.ECWy____________",
;;         :auth0/scope "openid profile",
;;         :auth0/expires-in 86400,
;;         :auth0/token-type "Bearer"},
;;  :trace-redirects []}