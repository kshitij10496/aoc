(ns aoc.2023.day19-test
  (:require [clojure.test :refer :all]
            [clojure.string :as clojure.string]
            [aoc.2023.day19 :as aplenty :refer :all]))

(deftest rating-sum-test 
  (testing "Sum of Ratings of a single part from example" 
    (is (= 7540 (aplenty/rating-sum (aplenty/make-part 787 2655 1222 2876))))
    (is (= 4623 (aplenty/rating-sum (aplenty/make-part 2036 264 79 2244))))
    (is (= 6951 (aplenty/rating-sum (aplenty/make-part 2127 1623 2188 1013))))))

(deftest parse-part-test 
  (testing "Context of the test assertions"
    (is (= (aplenty/make-part 787 2655 1222 2876) (aplenty/parse-part "{x=787,m=2655,a=1222,s=2876}")))
    (is (= (aplenty/make-part 2036 264 79 2244) (aplenty/parse-part "{x=2036,m=264,a=79,s=2244}")))
    (is (= (aplenty/make-part 2127 1623 2188 1013) (aplenty/parse-part "{x=2127,m=1623,a=2188,s=1013}"))))) 

(deftest parse-workflow-test
  (testing "Example")
  (is (= (aplenty/parse-workflow "in{s<1351:px,qqz}") (aplenty/make-workflow "in" [(aplenty/make-rule "s<1351" "px") (aplenty/make-rule true "qqz")])))
  (is (= (aplenty/parse-workflow "hdj{m>838:A,pv}") (aplenty/make-workflow "hdj" [(aplenty/make-rule "m>838" "A") (aplenty/make-rule true "pv")]))))