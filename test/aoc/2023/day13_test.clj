(ns aoc.2023.day13-test
  (:require [clojure.test :refer :all]
             [clojure.string :as clojure.string]
             [aoc.2023.day13 :as incidence :refer :all]))

((deftest row-count-test
      (testing "Context of the test assertions"
        (is (=  (incidence/row-count (clojure.string/split-lines "#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#")))))) )

