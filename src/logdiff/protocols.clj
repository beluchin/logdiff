(ns logdiff.protocols)

(defprotocol AllDiffIgnored?
  (all-diff-ignored? [line-diffs]))

(defprotocol LineDiffToString
  (to-string [line-diff]))

