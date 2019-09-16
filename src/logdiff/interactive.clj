(ns logdiff.interactive
  (:require [clojure.string :as str]
            [logdiff.interactive.app :as app])
  (:import (org.jline.reader LineReaderBuilder UserInterruptException)
           org.jline.terminal.TerminalBuilder))

;;;
;;; this namespace contains the jline-related code exclusively
;;;

(declare print-basic-commands output get-output)

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
            (output term (get-output line)))
          (if-not quit? (recur (.readLine reader "logdiff> ")))))
      (catch UserInterruptException _ (println "interrupted")))))

(defn- print-basic-commands []
  (println (str/join "\n" 
                     ["<enter> repeats the last command"
                      "n       show the next difference"
                      "p       show the previous difference"
                      ""
                      "q|quit  exits the program"
                      "help    list available commands"])))

(def ^:private functions
  {"n" app/next
   "p" app/previous})

(def ^:private last-command (atom nil))

(defn- get-last-command [] @last-command)

(defn- put-last [command] (reset! last-command command))

(defn- get-command [line]
  (if (empty? line)
    (get-last-command)
    (let [command (get functions line)]
      (put-last command)
      command)))

(defn- get-output [line]
  (let [command (get-command line)]
    (if (nil? command)
      :???
      (command))))

(defn- output [term text]
  (.. term (writer) (println (str "==> " text)))
  (.flush term))


(comment
  (contains? #{:a} :a)
  (get))
