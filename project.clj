(defproject logdiff "0.1.0-SNAPSHOT"
  :description "smart log diff utility"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main ^:skip-aot logdiff.core

  :dependencies [
                 ;; flycheck-clojure setup
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/core.typed "0.5.3"  :classifier "slim"]
                 [tortue/spy "1.4.0" :scope "test"]

                 [tempfile "0.2.0" :scope "test"]
                 [org.jline/jline "3.11.0"]
                 [org.fusesource.jansi/jansi "1.18"]
                 #_[net.java.dev.jna/jna "5.3.1"]]
                 
  :profiles {:uberjar {:aot :all}

             ;; Configuration here may be overridden by namespace metadata.
             :dev {:env {:squiggly {:checkers [:eastwood :typed :kibit]
                                    :eastwood-exclude-linters [:unlimited-use]
                                    :eastwood-options {:add-linters [:unused-fn-args]}}}}}
  ; to evaluate inside defproject ...
  ; https://stackoverflow.com/a/7739179/614800
  :target-path ~(str (let [os (System/getProperty "os.name")
                           is-windows (re-find #"(?i)windows" os)]
                       (if is-windows
                         "c:\\temp"
                         "/Users/beluchin/tmp"))
                     "/Users/beluchin/tmp"
                     "/logdiff__/target")

  :clean-targets ^{:protect false} ["c:\temp/Users/beluchin/tmp/logdiff__/target"]

  :jvm-opts ["-Djava.util.logging.config.file=log.properties"])
