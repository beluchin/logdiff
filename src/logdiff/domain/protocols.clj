(ns logdiff.domain.protocols)

(defprotocol AllDiffIgnored?
  (all-diff-ignored? [line-diffs]))
