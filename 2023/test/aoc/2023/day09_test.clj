(ns aoc.2023.day09-test
  (:require [clojure.test :refer :all]
            [aoc.2023.day09 :as oasis :refer :all]
            [clojure.set :as clojure.set :refer :all]))

(deftest diff-test
  (is (= [3 3 3 3 3] (oasis/diff [0 3 6 9 12 15])))
  (is (= [0 0 0 0] (oasis/diff [3 3 3 3 3])))
  (is (= [2 3 4 5 6] (oasis/diff [1 3 6 10 15 21])))
  (is (= [1 1 1 1] (oasis/diff [2 3 4 5 6])))
  (is (= [0 0 0] (oasis/diff [1 1 1 1])))
  (is (= [3 3 5 9 15] (oasis/diff [10 13 16 21 30 45])))
  (is (= [0 2 4 6] (oasis/diff [3 3 5 9 15])))
  (is (= [2 2 2] (oasis/diff [0 2 4 6])))
  (is (= [0 0] (oasis/diff [2 2 2]))))

(deftest zeros?-test
  (is (= false (oasis/zeros? [0 3 6 9 12 15])))
  (is (= false (oasis/zeros? [3 3 3 3 3])))
  (is (= false (oasis/zeros? [1 3 6 10 15 21])))
  (is (= false (oasis/zeros? [2 3 4 5 6])))
  (is (= false (oasis/zeros? [1 1 1 1])))
  (is (= false (oasis/zeros? [10 13 16 21 30 45])))
  (is (= false (oasis/zeros? [3 3 5 9 15])))
  (is (= false (oasis/zeros? [0 2 4 6])))
  (is (= false (oasis/zeros? [2 2 2])))
  (is (= true (oasis/zeros? [0 0 0 0 ])))
  (is (= true (oasis/zeros? [0 0 0])))
  (is (= true (oasis/zeros? [0 0]))))

(deftest predict-test
    (is (= 18 (oasis/predict [0 3 6 9 12 15])))
    (is (= 3 (oasis/predict [3 3 3 3 3])))
    (is (= 28 (oasis/predict [1 3 6 10 15 21])))
    (is (= 7 (oasis/predict [2 3 4 5 6])))
    (is (= 1 (oasis/predict [1 1 1 1])))
    (is (= 68 (oasis/predict [10 13 16 21 30 45])))
    (is (= 23 (oasis/predict [3 3 5 9 15])))
    (is (= 8 (oasis/predict [0 2 4 6])))
    (is (= 2 (oasis/predict [2 2 2])))
    (is (= 0 (oasis/predict [0 0 0 0 ])))
    (is (= 0 (oasis/predict [0 0 0])))
    (is (= 0 (oasis/predict [0 0]))))

(deftest extrapolate-test
    (is (= -3 (oasis/extrapolate [0 3 6 9 12 15])))
    (is (= 3 (oasis/extrapolate [3 3 3 3 3])))
    (is (= 0 (oasis/extrapolate [1 3 6 10 15 21])))
    (is (= 1 (oasis/extrapolate [2 3 4 5 6])))
    (is (= 1 (oasis/extrapolate [1 1 1 1])))
    (is (= 5 (oasis/extrapolate [10 13 16 21 30 45])))
    (is (= 5 (oasis/extrapolate [3 3 5 9 15])))
    (is (= -2 (oasis/extrapolate [0 2 4 6])))
    (is (= 2 (oasis/extrapolate [2 2 2])))
    (is (= 0 (oasis/extrapolate [0 0 0 0 ])))
    (is (= 0 (oasis/extrapolate [0 0 0])))
    (is (= 0 (oasis/extrapolate [0 0]))))

(deftest parse-sensor-data-test
    (is (= [0 3 6 9 12 15] (oasis/parse-sensor-data "0 3 6 9 12 15")))
    (is (= [1 3 6 10 15 21] (oasis/parse-sensor-data "1 3 6 10 15 21"))) 
    (is (= [10 13 16 21 30 45] (oasis/parse-sensor-data "10 13 16 21 30 45"))))

(deftest parse-input-test
    (is (= [[0 3 6 9 12 15] [1 3 6 10 15 21] [10 13 16 21 30 45]] (oasis/parse-input "0 3 6 9 12 15\n1 3 6 10 15 21\n10 13 16 21 30 45"))))

(deftest solve1-test
    (is (= 114 (oasis/solve1 "0 3 6 9 12 15\n1 3 6 10 15 21\n10 13 16 21 30 45"))))

(deftest solve2-test
    (is (= 2 (oasis/solve2 "0 3 6 9 12 15\n1 3 6 10 15 21\n10 13 16 21 30 45"))))