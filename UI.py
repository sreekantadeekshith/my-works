import os
os.environ["SWI_HOME_DIR"] = "C:\\Program Files\\swipl"
os.environ["PATH"] += ";C:\\Program Files\\swipl\\bin"

import customtkinter as ctk
from pyswip import Prolog

ctk.set_appearance_mode("dark")
ctk.set_default_color_theme("blue")

prolog = Prolog()
prolog.consult("loan.pl")

def check_loan():
    prolog.retractall("income(_,_)")
    prolog.retractall("credit_score(_,_)")
    prolog.retractall("job(_,_)")
    prolog.retractall("existing_loan(_,_)")

    name = "user"

    inc = int(income_entry.get())
    credit = int(credit_entry.get())
    job_val = job_option.get()
    loan_val = loan_option.get()

    # Insert facts
    prolog.assertz(f"income({name}, {inc})")
    prolog.assertz(f"credit_score({name}, {credit})")
    prolog.assertz(f"job({name}, {job_val})")
    prolog.assertz(f"existing_loan({name}, {loan_val})")

    score = list(prolog.query(f"total_score({name}, S)"))[0]['S']

    if list(prolog.query(f"approve({name})")):
        decision = "APPROVED"
        color = "#00C853"
    elif list(prolog.query(f"review({name})")):
        decision = "REVIEW"
        color = "#FF9800"
    else:
        decision = "REJECTED"
        color = "#D32F2F"

    result_label.configure(
        text=f"{decision}\nScore: {score}",
        text_color=color
    )


# App Window
app = ctk.CTk()
app.geometry("500x500")
app.title("AI Loan Approval")

# Title
title = ctk.CTkLabel(app, text="Loan Approval System",
                     font=("Arial", 22, "bold"))
title.pack(pady=20)

# Frame
frame = ctk.CTkFrame(app)
frame.pack(pady=10, padx=20, fill="both", expand=True)

# Inputs
income_entry = ctk.CTkEntry(frame, placeholder_text="Enter Income")
income_entry.pack(pady=10, padx=20, fill="x")

credit_entry = ctk.CTkEntry(frame, placeholder_text="Enter Credit Score")
credit_entry.pack(pady=10, padx=20, fill="x")

job_option = ctk.CTkOptionMenu(frame, values=["permanent", "temporary"])
job_option.pack(pady=10, padx=20, fill="x")

loan_option = ctk.CTkOptionMenu(frame, values=["yes", "no"])
loan_option.pack(pady=10, padx=20, fill="x")

# Button
check_btn = ctk.CTkButton(frame, text="Check Eligibility",
                         command=check_loan)
check_btn.pack(pady=20)

# Result
result_label = ctk.CTkLabel(app, text="", font=("Arial", 18, "bold"))
result_label.pack(pady=20)

app.mainloop()