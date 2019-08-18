(ns logdiff.console.interactive.domain
  (:require [logdiff.domain.internal :as internal]))

(defn logdiffline [lhs rhs rules]
  (internal/logdiffline-internal lhs rhs (internal/trans-preds rules)))
