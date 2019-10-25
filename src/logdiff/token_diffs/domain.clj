(ns logdiff.token-diffs.domain
  (:require [logdiff.domain.protocols :as protocols])
  (:import logdiff.domain.model.TokenDiffs))

(extend-protocol protocols/AllDiffIgnored?
  TokenDiffs
  (all-diff-ignored? [this]
    (every? #(:ignored %) (remove string? (:diffs this)))))

