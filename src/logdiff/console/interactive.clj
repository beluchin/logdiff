(ns logdiff.console.interactive
  (:require [clojure.string :as str]
            [logdiff.console.interactive.app :as app])
  (:import org.jline.reader.LineReaderBuilder
           org.jline.terminal.TerminalBuilder))

(declare print-basic-commands output execute)

(defn interactive [lhs rhs]
  (app/interactive lhs rhs)
  (let [term-builder (doto (TerminalBuilder/builder) (.system true))
        term (.build term-builder)
        reader (.. LineReaderBuilder (builder) (terminal term) (build))]

    (println "help: list available commands")

    (loop [line (.readLine reader "logdiff> ")]
      (let [quit? (contains? #{"q" "quit"} line)]
        (case line
          "help" (print-basic-commands)
          ("q" "quit") nil 
          (when-not (empty? line)
            (output term (execute line))))
        (if-not quit? (recur (.readLine reader "logdiff> ")))))))

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
