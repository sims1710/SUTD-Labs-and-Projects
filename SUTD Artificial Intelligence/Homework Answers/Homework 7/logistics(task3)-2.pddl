;Problem File

(define (problem delivery3-problem)
  (:domain delivery3-strips)
  (:objects changi tampines bedok truck1 package1 package2)
  (:init
    (truck truck1)
    (package package1)
    (package package2)
    (at-truck truck1 tampines)
    (at-package package1 bedok)
    (at-package package2 changi))
  (:goal (and
           (at-package package1 changi)
           (at-package package2 bedok))))