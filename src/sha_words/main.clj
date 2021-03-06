(ns sha-words.main
  (:require [sha-words.words :refer [word-list]]
            [clojure.string :as str])
  (:gen-class))

(defn bit-rotate [x size k]
  (bit-or (bit-shift-right x k)
          (bit-shift-left x (- size k))))

(defn sha-word [sha]
  (let [v (.longValue (BigInteger. sha 16))
        n (int (Math/ceil (/  (* 64 (Math/log 2)) (Math/log (count word-list)))))
        ;; convert to n values in the range of the wordlist
        vs (take 5 (iterate #(bit-xor % (Long/rotateRight % 4)) v))]
    (->> vs
        (map #(nth word-list (mod % (count word-list)) vs))
        (str/join "-"))))


(defn -main [& [sha]]
  (if-not sha
    (do
      (print "Syntax: sha-words sha512")
      (System/exit 1))
    (try (println (sha-word sha))
         (catch NumberFormatException e
           (print "Sha512 value must be a hex number")
           (System/exit 2)))))
