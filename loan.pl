% Dynamic facts
:- dynamic income/2, credit_score/2, job/2, existing_loan/2.

% Individual scoring rules
income_score(X, 40) :- income(X, I), I > 50000.
income_score(X, 10) :- income(X, I), I =< 50000.

credit_score_val(X, 40) :- credit_score(X, C), C > 700.
credit_score_val(X, 20) :- credit_score(X, C), C =< 700, C >= 600.
credit_score_val(X, 5)  :- credit_score(X, C), C < 600.

job_score(X, 20) :- job(X, permanent).
job_score(X, 5)  :- job(X, temporary).

loan_penalty(X, -20) :- existing_loan(X, yes).
loan_penalty(X, 0)   :- existing_loan(X, no).

% Total score calculation
total_score(X, S) :-
    income_score(X, S1),
    credit_score_val(X, S2),
    job_score(X, S3),
    loan_penalty(X, S4),
    S is S1 + S2 + S3 + S4.

% Decision rules
approve(X) :- total_score(X, S), S >= 80.
review(X)  :- total_score(X, S), S >= 50, S < 80.
reject(X)  :- total_score(X, S), S < 50.