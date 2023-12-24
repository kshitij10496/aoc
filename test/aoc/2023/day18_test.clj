(ns aoc.2023.day18-test
  (:require [clojure.test :refer :all]
             [clojure.string :as clojure.string]
             [aoc.2023.day18 :as lagoon :refer :all]))

(deftest determinant-test 
   (testing "Example from Shoelace Wikipedia" 
     (is (= -17 (lagoon/determinant (lagoon/make-point 1 6) (lagoon/make-point 3 1))))
     (is (= 20 (lagoon/determinant (lagoon/make-point 7 2) (lagoon/make-point 4 4))))))

(deftest shoelace-test
  (testing "Example from Shoelace Wikipedia"
    (is (= 16.5 (lagoon/shoelace (lagoon/make-point 1 6) 
                                 [(lagoon/make-point 3 1)
                                 (lagoon/make-point 7 2)
                                 (lagoon/make-point 4 4)
                                 (lagoon/make-point 8 5)])))
    (is (= 4 (lagoon/shoelace (lagoon/make-point 0 0)
                                 [(lagoon/make-point 2 0)
                                 (lagoon/make-point 2 -2)
                                 (lagoon/make-point 0 -2)])))))

(deftest move-test
  (testing "Example"
    (is (= (lagoon/make-point 6 0) (lagoon/move (lagoon/make-point 0 0) "R" 6)))
    (is (= (lagoon/make-point 6 -5) (lagoon/move (lagoon/make-point 6 0) "D" 5)))
    (is (= (lagoon/make-point 4 -5) (lagoon/move (lagoon/make-point 6 -5) "L" 2)))
    (is (= (lagoon/make-point 4 -7) (lagoon/move (lagoon/make-point 4 -5) "D" 2)))
    (is (= (lagoon/make-point 6 -7) (lagoon/move (lagoon/make-point 4 -7) "R" 2)))
    ;; (is (= (lagoon/make-point 6 -5) (lagoon/move (lagoon/make-point 0 0) "D" 5)))
    ;; (is (= (lagoon/make-point 6 -5) (lagoon/move (lagoon/make-point 0 0) "D" 5)))
    ;; (is (= (lagoon/make-point 6 -5) (lagoon/move (lagoon/make-point 0 0) "D" 5)))
    ;; (is (= (lagoon/make-point 6 -5) (lagoon/move (lagoon/make-point 0 0) "D" 5)))
    ;; (is (= (lagoon/make-point 6 -5) (lagoon/move (lagoon/make-point 0 0) "D" 5)))
    ))

(deftest polygon-test
  (testing "Example"
     (is (= [(lagoon/make-point 0 0) (lagoon/make-point 6 0) (lagoon/make-point 6 -5) (lagoon/make-point 4 -5)] 
            (lagoon/polygon [(lagoon/make-instruction "R" 6) (lagoon/make-instruction "D" 5) (lagoon/make-instruction "L" 2)])))))