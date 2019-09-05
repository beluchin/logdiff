(ns logdiff.tempfile
  (:import [java.io File]
           [org.apache.commons.io FileUtils]))

(defmacro with-filenames [bindings & body]
  (cond
    (= (count bindings) 0) `(do ~@body)
    (symbol? (bindings 0)) `(let [~(bindings 0)
                                  (.getAbsolutePath (tempfile.core/tempfile ~(bindings 1))) ]
                              (try
                                (with-filenames ~(subvec bindings 2) ~@body)
                                (finally
                                  (FileUtils/forceDelete (File. ~(bindings 0))))))
    :else (throw (IllegalArgumentException.
                  "only symbols in bindings"))))
