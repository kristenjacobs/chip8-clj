(ns chip8-clj.test-FX07
  (:require [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest get-font-addr-test
  (machine-state/set-delay-timer 84)
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0xF507))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-register post-machine-state 5) 84))
    (is (= (machine-state/get-pc post-machine-state) 2))))

