(ns logdiff.domain.model)

(defrecord WholeLineDiff [lhs rhs])
(defrecord TokenDiffs [diffs])
