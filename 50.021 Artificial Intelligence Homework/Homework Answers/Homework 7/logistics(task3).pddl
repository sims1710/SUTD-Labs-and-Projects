;Domain File

(define (domain delivery3-strips)
  (:predicates 
    (truck ?t)
    (package ?p)
    (at-truck ?t ?loc)
    (at-package ?p ?loc))

  (:action drive
    :parameters (?from ?to ?t)
    :precondition (and (truck ?t) (at-truck ?t ?from))
    :effect (and (at-truck ?t ?to) (not (at-truck ?t ?from))))

  (:action load
    :parameters (?p ?loc ?t)
    :precondition (and (package ?p) (truck ?t) (at-package ?p ?loc) (at-truck ?t ?loc))
    :effect (and (not (at-package ?p ?loc)) (at-package ?p ?t)))

  (:action unload
    :parameters (?p ?loc ?t)
    :precondition (and (package ?p) (truck ?t) (at-truck ?t ?loc) (at-package ?p ?t))
    :effect (and (at-package ?p ?loc) (not (at-package ?p ?t))))
)