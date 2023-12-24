(ns aoc.2023.day24-test
  (:require [clojure.test :refer :all]
            [clojure.string :as clojure.string]
            [aoc.2023.day24 :as odds :refer :all]))


;; (deftest foo-test
;;       (testing "Context of the test assertions"
;;         (is (= [14.333, 15.333] (odds/foo (odds/build-path 19 13 30 -2 1 -2) (odds/build-path 18 19 22 -1 -1 -2))))))

(deftest parallel?-test
  (is (= true (odds/parallel? (odds/build-path 18 19 22 -1 -1 -2) (odds/build-path 20 25 34 -2 -2 -4))))
  (is (= false (odds/parallel? (odds/build-path 19 13 30 -2 1 -2) (odds/build-path 18 19 22 -1 -1 -2)))))

(deftest poi-test
  (is (= [(/ 43 3), (/ 46 3)] (odds/poi (odds/build-path 19 13 30 -2 1 -2) (odds/build-path 18 19 22 -1 -1 -2)))))

(deftest build-path-test
  (let [l (odds/build-path 19 13 30 -2 1 -2)]
    (is (= (/ 1 (- 2)) (odds/slope l)))
    (is (= (/ 45 2) (odds/x-intercept l)))))

(deftest parse-line-test
  (let [got (odds/parse-line "19, 13, 30 @ -2,  1, -2")]
    (is (= (/ 1 (- 2)) (odds/slope got)))
    (is (= (/ 45 2) (odds/x-intercept got)))))

(deftest solve1-test
  (let [input "19, 13, 30 @ -2,  1, -2
18, 19, 22 @ -1, -1, -2
20, 25, 34 @ -2, -2, -4
12, 31, 28 @ -1, -2, -1
20, 19, 15 @  1, -5, -3"]
    (is (= 2 (odds/solve1 input)))))