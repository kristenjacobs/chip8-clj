(defproject chip8-clj "0.1.0-SNAPSHOT"
  :description "Clojure based chip8 emulator"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [seesaw "1.4.5"]
                 [quil "2.4.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [ch.qos.logback/logback-classic "1.1.3"]]
  :plugins      [[lein-cloverage "1.0.6"]]
  :main ^:skip-aot chip8-clj.main
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
