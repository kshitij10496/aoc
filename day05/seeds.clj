;; seeds.clj --- Scratchcards
(ns seeds
  (:require [clojure.string]))

(def input "seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4")


(defn contains-idx [i start end]
  (and (>= i start) (< i (+ start end))))


(defn get-nth-item [ranges idx]
  ;; {:source-start 98 :dest-start 50 :range-length 2}
  ;; {:source-start 50 :dest-start 52 :range-length 48}
  (let [range (first (filter #(contains-idx idx (get % :source-start) (get % :range-length)) ranges))]
    (if (nil? range)
      idx
      (+ (get range :dest-start) (- idx (get range :source-start))))))

(defn get-items-with-index [items idxs]
  (->> idxs
       (pmap #(get-nth-item items %))
       (vec)))

(defn build-seed-seq [seed]
  (let [start (get seed :start)
        range-length (get seed :range-length)]
    ;; (println start range-length)
    (range start (+ start range-length))))

(defn parse-seed-ranges [s]
  (for [idx (range (count s))]
    (if (even? idx) 
      {:start (nth s idx)
       :range-length (nth s (inc idx))})))


(defn parse-seeds [text]
  (let [lines (clojure.string/split-lines text)
        matcher #"seeds: (.*)"
        m (re-matches matcher (first lines))
        seed-vals (->> (clojure.string/split (get m 1) #" ")
               (filter #(not (clojure.string/blank? %)))
               (map #(Long/parseLong %))) 
        seed-ranges (parse-seed-ranges seed-vals)]
    (->> seed-ranges
         (filter #(not (nil? %)))
         (map build-seed-seq))))


(defn parse-data [line]
  (let [matcher #"(\d+) (\d+) (\d+)"
        m (re-matches matcher line)
        dest-start (Long/parseLong (get m 1))
        source-start (Long/parseLong (get m 2))
        range-length (Long/parseLong (get m 3))] 
    {:source-start source-start
     :dest-start dest-start
     :range-length range-length}))


(defn parse-map [text]
  (let [lines (clojure.string/split-lines text)
        matcher #"(seed|soil|fertilizer|water|light|temperature|humidity)-to-(soil|fertilizer|water|light|temperature|humidity|location) map:"
        m (re-matches matcher (first lines))
        source (get m 1)
        dest (get m 2)
        data (->> (rest lines)
                  (filter #(not (clojure.string/blank? %)))
                  (map parse-data)
                  (filter #(not (empty? %)))
                  )]
    {:source source
     :dest dest
     :data data }))

(defn parse-maps [text]
  (let [maps (clojure.string/split text #"\n\n")]
    (->> maps
         (map parse-map)
         (filter #(not (empty? (:data %)))))))

(defn process-seeds-seq [maps seeds]
    (loop [maps maps
           result seeds]
      (if (empty? maps)
        result
        (let [map (first maps)
              items (get map :data)]
          (recur (rest maps) (get-items-with-index items result))))))


(defn engine [input]
  (let [seeds (parse-seeds input)
        maps (parse-maps input)] 
  (reduce min (->> seeds
       (filter #(not (nil? %)))
       (pmap #(process-seeds-seq maps %)) 
       (pmap #(apply min %))))))

(defn solve [path]
  (engine (slurp path)))
