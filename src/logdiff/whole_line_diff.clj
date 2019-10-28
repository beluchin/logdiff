(ns logdiff.whole-line-diff
  (:require [logdiff.protocols :as protocols])
  (:import logdiff.domain.model.WholeLineDiff))

(extend-type WholeLineDiff
  protocols/AllDiffIgnored?
  (all-diff-ignored? [_] false)

  protocols/LineDiffToString
  (to-string [this] (format "[-[-%s-]-]{+{+%s+}+}" (:lhs this) (:rhs this))))

