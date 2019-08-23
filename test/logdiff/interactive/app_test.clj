(ns logdiff.interactive.app-test
  (:require [clojure.test :as t]
            [logdiff.interactive.app :as sut]
            [logdiff.tempfile :as tempfile]))

(declare interactive)

(t/deftest nexts
  (do    
    (interactive "a1\na2" "b1\nb2")
    (t/is (= "[-a1-]{+b1+}" (sut/next)))
    (t/is (= "[-a2-]{+b2+}" (sut/next)))
    (t/is (= :no-more-diffs (sut/next)))))

(defn- interactive [lhstext rhstext]
  (tempfile/with-paths [l lhstext
                        r rhstext]
    (sut/interactive l r)))
