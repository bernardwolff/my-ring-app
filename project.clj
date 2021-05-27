(defproject my-ring-app "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring/ring-core "1.9.3"]
                 [ring/ring-jetty-adapter "1.9.3"]
                 [ring/ring-devel "1.9.3"]
                 [auth0-clojure "0.1.0"]]
  :repl-options {:init-ns my-ring-app.core}
  :ring {:handler my-ring-app.core/app}
  :plugins [[lein-ring "0.12.5"]])