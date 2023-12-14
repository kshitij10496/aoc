(ns aoc.2023.day14-test
  (:require [clojure.test :refer :all]
             [clojure.string :as clojure.string]
             [aoc.2023.day14 :as reflectors :refer :all]))

(deftest count-rocks-test
      (testing "Context of the test assertions"
        (is (= 6 (reflectors/count-rocks "..00.#0..0.0#..0")))
        (is (= 2 (reflectors/count-rocks "..00.")))
        ))

(deftest tilt-segment-test
      (testing "Context of the test assertions"
        (is (= "00..." (reflectors/tilt-segment "..00.")))
        (is (= "000..." (reflectors/tilt-segment "0..0.0")))
        (is (= "0.." (reflectors/tilt-segment "..0"))))) 

(deftest segments-test
      (testing "Context of the test assertions"
        (is (= "00...#000...#0.." (reflectors/segments "..00.#0..0.0#..0")))))

