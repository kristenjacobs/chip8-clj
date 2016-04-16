(ns chip8-clj.test-FX18
  (:require [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest get-font-addr-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0xF518)
                              (machine-state/set-register 5 72))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-sound-timer) 72))
    (is (= (machine-state/get-pc post-machine-state) 2))))

