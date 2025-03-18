;Problem File

(define (problem delivery-problem)
  (:domain delivery-strips)
  (:objects changi tampines bedok truck1 package1)
  (:init
    (truck truck1)
    (package package1)
    (at-truck truck1 tampines)
    (at-package package1 bedok))
  (:goal (at-package package1 changi))
)
