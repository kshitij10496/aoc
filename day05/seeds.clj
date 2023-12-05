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

(defn get-nth-item [items n]
  (if (< n (count items)) (nth items n) n))

(defn get-items-with-index [items idxs]
  (->> idxs
       (map #(get-nth-item items %))
       (vec)))

(defn parse-seeds [text]
  (let [lines (clojure.string/split-lines text) 
        matcher #"seeds: (.*)"
        m (re-matches matcher (first lines))] 
    (vec (map #(Long/parseLong %) (clojure.string/split (get m 1) #" ")))))

(defn parse-data [line]
  (let [matcher #"(\d+) (\d+) (\d+)"
        m (re-matches matcher line)
        dest-start (Long/parseLong (get m 1))
        source-start (Long/parseLong (get m 2))
        range-length (Long/parseLong (get m 3))] 
    {:source-start source-start
     :dest-start dest-start
     :range-length range-length}))

(defn build-default-map [i n start]
  (flatten (conj (vec (range i))
                 (for [i (range n)] (+ start i)))))

(defn merge-2-maps [a b]
  (let [v1 (if (> (count a) (count b)) a b)
        v2 (if (> (count a) (count b)) b a)]
        (->> v1
         (map-indexed (fn [i x] [i x]))
         (map (fn [[i x]] (if (or (>= i (count v2)) (not (= i x)) (nil? (nth v2 i))) x
                                  (nth v2 i)))))))

(defn merge-maps [& maps]
  (loop [maps maps
         result (first maps)]
    (if (empty? maps)
      result
      (recur (rest maps) (merge-2-maps result (first maps))))))

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
                  (map #(build-default-map (get % :source-start) (get % :range-length) (get % :dest-start)))
                  (apply merge-maps))]
    {:source source
     :dest dest
     :data data }))

(defn parse-maps [text]
  (let [maps (clojure.string/split text #"\n\n")]
    (->> maps
         (map parse-map)
         (filter #(not (empty? (:data %)))))))

(defn process [input]
  (let [seeds (parse-seeds input)
        maps (parse-maps input)] 
    (loop [maps maps
           result seeds]
      (if (empty? maps)
        result
        (let [map (first maps)
              items (get map :data)]
          (recur (rest maps) (get-items-with-index items result)))))))

(defn engine [input]
  (apply min (process input)))

(defn solve [path]
  (engine (slurp path)))