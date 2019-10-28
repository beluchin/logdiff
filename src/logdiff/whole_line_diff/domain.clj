(ns logdiff.whole-line-diff.domain
  (:require [logdiff.domain.protocols :as protocols])
  (:import logdiff.domain.model.WholeLineDiff))

(extend-protocol protocols/AllDiffIgnored?
  WholeLineDiff
  (all-diff-ignored? [_] false))

