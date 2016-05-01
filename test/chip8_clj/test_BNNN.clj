(ns chip8-clj.test-BNNN
  (:require [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest jump-immediate-add-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0xB246)
                              (machine-state/set-register 0 0x10))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-pc post-machine-state) 0x256))))

