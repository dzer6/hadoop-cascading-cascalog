(use 'cascalog.playground) (bootstrap)


(def age [ ;; [person   age]
              ["alice"  28]
              ["bob"    33]
              ["chris"  40]
              ["david"  25]
              ["emily"  25]
              ["george" 31]
              ["gary"   28]
              ["kumar"  27]
              ["luanne" 36]])


(?<- (stdout) [?person] 
              (age ?person 25))


(?<- (stdout) [?person] 
              (age ?person ?age) 
              (< ?age 30))


(?<- (stdout) [?person ?age] 
              (age ?person ?age)
              (< ?age 30))


(?<- (stdout) [?person] 
              (follows "emily" ?person)
              (gender ?person "m"))

