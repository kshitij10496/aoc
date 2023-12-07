(ns aoc.2023.day01 
  (:require [clojure.string]))

(defn normalise
  "Normalises the input string by replacing spelled out digits into corresponding digits."
  [s]
  (let [digit-map {"one" "1"
                   "two" "2"
                   "three" "3"
                   "four" "4"
                   "five" "5"
                   "six" "6"
                   "seven" "7"
                   "eight" "8"
                   "nine" "9"}
        matcher #"one|two|three|four|five|six|seven|eight|nine"]
    (clojure.string/replace s matcher digit-map)))

(defn normalise-rev
  "Normalises the input string by replacing spelled out digits into corresponding digits."
  [s]
  (let [digit-map {"eno" "1"
                   "owt" "2"
                   "eerht" "3"
                   "ruof" "4"
                   "evif" "5"
                   "xis" "6"
                   "neves" "7"
                   "thgie" "8"
                   "enin" "9"}
        matcher #"eno|owt|eerht|ruof|evif|xis|neves|thgie|enin"]
    (clojure.string/replace s matcher digit-map)))

(defn first-digit
  "Returns the first digit found in the string."
  [s]
  (Integer. (re-find #"\d" s)))

(defn calibration-value
  "Returns a 2 digit number from the given string.
   The calibration value can be found by combining the first digit and the last digit (in that order) to form a single two-digit number."
  [s]
  (+ (* 10 (first-digit s))
     (first-digit (apply str (reverse s)))))


(defn calibration-value-with-normalisation
  "Returns a 2 digit number from the given string.
   The calibration value can be found by combining the first digit and the last digit (in that order) to form a single two-digit number."
  [s]
  (+ (* 10 (first-digit (normalise s)))
     (first-digit (normalise-rev (apply str (reverse s))))))

(defn sum-calibrations
  "Computes the sum of calibrations given the path a file containing the inputs."
  [calibration input]
  (reduce + (map calibration input)))

(defn main
  "Main entry point."
  [path]
  (if (empty? path)
    (println "Usage: day01.clj <input-file>") 
    (do
      (println "Part 1:" (sum-calibrations calibration-value (clojure.string/split-lines (slurp path))))
      (println "Part 2:" (sum-calibrations calibration-value-with-normalisation (clojure.string/split-lines (slurp path)))))))
