(ns logdiff.interactive
  (:require [clojure.string :as str]
            [logdiff.interactive.app :as app])
  (:import (org.jline.reader LineReaderBuilder UserInterruptException)
           org.jline.terminal.TerminalBuilder))

;;;
;;; this namespace contains the jline-related code exclusively
;;;

(declare print-basic-commands output execute)

(defn run [lhs rhs]
  (app/init lhs rhs)
  (let [term-builder (doto (TerminalBuilder/builder) (.system true))
        term (.build term-builder)
        reader (.. LineReaderBuilder (builder) (terminal term) (build))]

    (println "help: list available commands")

    (try 
      (loop [line (.readLine reader "logdiff> ")]
        (let [quit? (contains? #{"q" "quit"} line)]
          (case line
            "help" (print-basic-commands)
            ("q" "quit") nil 
            (when-not (empty? line)
              (output term (execute line))))
          (if-not quit? (recur (.readLine reader "logdiff> ")))))
      (catch UserInterruptException _ (println "interrupted")))))

(defn- print-basic-commands []
  (println (str/join "\n" 
                     ["n      show the next difference"
                      "q|quit exits the program"
                      "help   list available commands"])))

(def ^:private functions {"n" app/next})

(defn- execute [line]
  ((get functions line)))

(defn- output [term text]
  (.. term (writer) (println (str "==> " text)))
  (.flush term))


(comment
  (contains? #{:a} :a)
  (get))
