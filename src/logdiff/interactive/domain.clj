(ns logdiff.interactive.domain
  (:require [logdiff.domain.internal :as internal]))

(defn loglinediff [lhs rhs rules]
  (internal/logdiffline-internal lhs rhs (internal/trans-preds rules)))
