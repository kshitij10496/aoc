(ns aoc.2024.day01
  (:require [clojure.string]))

(defn calibration-value
  "Returns a 2 digit number from the given string.
   The calibration value can be found by combining the first digit and the last digit (in that order) to form a single two-digit number."
  [s]
  (println s))

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
      (println "Part 1:" (sum-calibrations calibration-value (clojure.string/split-lines (slurp path)))))))
