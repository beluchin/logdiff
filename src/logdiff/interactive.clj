(ns logdiff.interactive
  (:require [clojure.string :as str]
            [logdiff.interactive.app :as app])
  (:import (org.jline.reader LineReaderBuilder UserInterruptException)
           org.jline.terminal.TerminalBuilder))

;;;
;;; this namespace contains the jline-related code exclusively
;;;

(declare print-usage output get-output readLine)

(defn run [lhs rhs]
  (app/init lhs rhs)
  (let [term-builder (doto (TerminalBuilder/builder) (.system true))
        term (.build term-builder)
        reader (.. LineReaderBuilder (builder) (terminal term) (build))
        readLine #(.trim (.readLine reader "logdiff> "))]

    (println "help: list available commands")

    (try 
      (loop [line (readLine)]
        (let [quit? (contains? #{"q" "quit"} line)]
          (when-not quit?
            (output term (get-output line))
            (recur (readLine)))))
      (catch UserInterruptException _ (println "interrupted")))))

(defn- print-usage []
  (println (str/join "\n" 
                     ["n       show the next difference"
                      "p       show the previous difference"
                      ""
                      "i       toggle showing identical lines (default don't show)"
                      ""
                      "<enter> repeats the last command"
                      "help    list available commands"
                      "q|quit  exits the program"])))

(def ^:private functions
  {"i"    app/toggle-showing-identical
   "n"    app/next
   "p"    app/previous
   "help" print-usage})

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
