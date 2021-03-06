(ns chip8-clj.test-EX9E
  (:require [chip8-clj.machine-state :as machine-state]
            [chip8-clj.state :as state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest key-pressed-true-test-1
  (with-redefs-fn {#'state/is-key-pressed (fn [n] true)}
    #(let [pre-machine-state (-> (machine-state/initialise)
                                 (machine-state/set-pc 0)
                                 (machine-state/set-instr 0 0xE19E)
                                 (machine-state/set-register 1 0x1))
           post-machine-state (step pre-machine-state)]
       (is (= (machine-state/get-pc post-machine-state) 4)))))

(deftest key-pressed-true-test-2
  (with-redefs-fn {#'state/is-key-pressed (fn [n] false)}
    #(let [pre-machine-state (-> (machine-state/initialise)
                                 (machine-state/set-pc 0)
                                 (machine-state/set-instr 0 0xE19E)
                                 (machine-state/set-register 1 0x1))
           post-machine-state (step pre-machine-state)]
       (is (= (machine-state/get-pc post-machine-state) 2)))))

