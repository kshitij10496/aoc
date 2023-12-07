(ns aoc.2023.day07-test
  (:require [clojure.test :refer :all]
            [aoc.2023.day07 :as camel-cards :refer :all]))

(deftest card-to-strength-test
  (testing "Order of card strength"
    (is (> (:A camel-cards/card-to-strength)
           (:K camel-cards/card-to-strength)
           (:Q camel-cards/card-to-strength)
           (:J camel-cards/card-to-strength)
           (:T camel-cards/card-to-strength)
           (:9 camel-cards/card-to-strength)
           (:8 camel-cards/card-to-strength)
           (:7 camel-cards/card-to-strength)
           (:6 camel-cards/card-to-strength)
           (:5 camel-cards/card-to-strength)
           (:4 camel-cards/card-to-strength)
           (:3 camel-cards/card-to-strength)
           (:2 camel-cards/card-to-strength)))))

(deftest card-to-strength-test
  (testing "Order of card strength with joker"
    (is (> (:A camel-cards/card-to-strength-with-joker)
           (:K camel-cards/card-to-strength-with-joker)
           (:Q camel-cards/card-to-strength-with-joker) 
           (:T camel-cards/card-to-strength-with-joker)
           (:9 camel-cards/card-to-strength-with-joker)
           (:8 camel-cards/card-to-strength-with-joker)
           (:7 camel-cards/card-to-strength-with-joker)
           (:6 camel-cards/card-to-strength-with-joker)
           (:5 camel-cards/card-to-strength-with-joker)
           (:4 camel-cards/card-to-strength-with-joker)
           (:3 camel-cards/card-to-strength-with-joker)
           (:2 camel-cards/card-to-strength-with-joker)
           (:J camel-cards/card-to-strength-with-joker)))))

(deftest hand-type-to-strength-test
  (testing "Order of Hand Type strength"
    (is (> (:five-of-a-kind camel-cards/hand-type-to-strength)
           (:four-of-a-kind camel-cards/hand-type-to-strength)
           (:full-house camel-cards/hand-type-to-strength)
           (:three-of-a-kind camel-cards/hand-type-to-strength)
           (:two-pair camel-cards/hand-type-to-strength)
           (:one-pair camel-cards/hand-type-to-strength)
           (:high-card camel-cards/hand-type-to-strength)))))

(deftest compare-cards-char-by-char-test
  (testing "Compare cards by strength"
    (is (= 1 (camel-cards/compare-cards-char-by-char \A \K))) 
    (is (= 0 (camel-cards/compare-cards-char-by-char \A \A)))
    (is (= -1 (camel-cards/compare-cards-char-by-char \K \A)))))

(deftest compare-hands-first-order-test
  (testing "first ordering rule"
    (is (= 1 (camel-cards/compare-hands-first-order :two-pair :one-pair)))
    (is (= 0 (camel-cards/compare-hands-first-order :two-pair :two-pair))))
    (is (= 1 (camel-cards/compare-hands-first-order :three-of-a-kind :two-pair)))
    (is (= 0 (camel-cards/compare-hands-first-order :three-of-a-kind :three-of-a-kind))))

(deftest compare-hands-second-order-test
  (testing "second ordering rule" 
    (is (= 1 (camel-cards/compare-hands-second-order "33332" "2AAAA")))
    (is (= 1 (camel-cards/compare-hands-second-order "77888" "77788"))))
    (is (= 1 (camel-cards/compare-hands-second-order "KK677" "KTJJT")))
    (is (= -1 (camel-cards/compare-hands-second-order "T55J5" "QQQJA"))))

(deftest compare-hands-test
  (testing "fallback to second ordering rule for hands of same type"
    (is (= 1 (camel-cards/compare-hands {:hand "33332" :type :four-of-a-kind} {:hand "2AAAA" :type :four-of-a-kind})))
    (is (= 1 (camel-cards/compare-hands {:hand "77888" :type :three-of-a-kind} {:hand "77788" :type :three-of-a-kind})))))

(deftest get-hand-type-test
  (testing "get-hand-type without joker"
    (let [builder (camel-cards/get-builder false)]
      (is (= :one-pair (camel-cards/get-hand-type "32T3K" builder)))
      (is (= :two-pair (camel-cards/get-hand-type "KK677" builder)))
      (is (= :two-pair (camel-cards/get-hand-type "KTJJT" builder)))
      (is (= :three-of-a-kind (camel-cards/get-hand-type "T55J5" builder)))
      (is (= :three-of-a-kind (camel-cards/get-hand-type "QQQJA" builder))))))

(deftest bid-to-winnings-test
      (testing "Winnings computation for example input"
        (is (= 6440 (camel-cards/bid-to-winnings [765 220 28 684 483]))) 
        ))

(deftest winnings-test
   (testing "Winnings computation for example hands"
     (is (= 6440 (camel-cards/winnings [{:hand "32T3K" :bid 765}
                                        {:hand "KTJJT" :bid 220}
                                        {:hand "KK677" :bid 28}
                                        {:hand "T55J5" :bid 684}
                                        {:hand "QQQJA" :bid 483}])))) 
  (testing "Winnings with joker"
    (is (= 5905 (camel-cards/winnings [{:hand "32T3K" :bid 765 :type :one-pair}
                                       {:hand "KK677" :bid 28 :type :two-pair}
                                       {:hand "T55J5" :bid 684 :type :four-of-a-kind}
                                       {:hand "QQQJA" :bid 483 :type :four-of-a-kind}
                                       {:hand "KTJJT" :bid 220 :type :four-of-a-kind}])))))

(deftest rank-hands-test
  (testing "Rank hands from example"
    (is (= [{:hand "32T3K" :bid 765 :type :one-pair}
            {:hand "KTJJT" :bid 220 :type :two-pair}
            {:hand "KK677" :bid 28 :type :two-pair}
            {:hand "T55J5" :bid 684 :type :three-of-a-kind}
            {:hand "QQQJA" :bid 483 :type :three-of-a-kind}] (camel-cards/rank-hands [{:hand "32T3K" :bid 765 :type :one-pair}
                                                               {:hand "T55J5" :bid 684 :type :three-of-a-kind}
                                                               {:hand "KK677" :bid 28 :type :two-pair}
                                                               {:hand "KTJJT" :bid 220 :type :two-pair}
                                                               {:hand "QQQJA" :bid 483 :type :three-of-a-kind}])))))


(deftest parse-hand-test
  (testing "Parse hand without joker"
    (let [builder (camel-cards/get-builder false)]
    (is (= {:hand "32T3K" :bid 765 :type :one-pair} (camel-cards/parse-hand "32T3K 765" builder)))
    (is (= {:hand "T55J5" :bid 684 :type :three-of-a-kind } (camel-cards/parse-hand "T55J5 684" builder)))
    (is (= {:hand "KK677" :bid 28 :type :two-pair} (camel-cards/parse-hand "KK677 28" builder)))
    (is (= {:hand "KTJJT" :bid 220 :type :two-pair} (camel-cards/parse-hand "KTJJT 220" builder)))
    (is (= {:hand "QQQJA" :bid 483 :type :three-of-a-kind} (camel-cards/parse-hand "QQQJA 483" builder))))))

(deftest parse-input-test
  (testing "Parse input"
    (is (= [{:hand "32T3K" :bid 765 :type :one-pair}
            {:hand "T55J5" :bid 684 :type :three-of-a-kind}
            {:hand "KK677" :bid 28 :type :two-pair}
            {:hand "KTJJT" :bid 220 :type :two-pair}
            {:hand "QQQJA" :bid 483 :type :three-of-a-kind}] (camel-cards/parse-input "32T3K 765\nT55J5 684\nKK677 28\nKTJJT 220\nQQQJA 483")))))

(deftest parse-input-with-joker-test
  (testing "Parse input with Joker"
    (is (= [{:hand "32T3K" :bid 765 :type :one-pair} 
            {:hand "T55J5" :bid 684 :type :four-of-a-kind}
            {:hand "KK677" :bid 28 :type :two-pair}
            {:hand "KTJJT" :bid 220 :type :four-of-a-kind}
            {:hand "QQQJA" :bid 483 :type :four-of-a-kind}] (camel-cards/parse-input-with-joker "32T3K 765\nT55J5 684\nKK677 28\nKTJJT 220\nQQQJA 483")))))