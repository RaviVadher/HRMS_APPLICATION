import { useState } from "react";
import { TextField, Button } from "@mui/material";
import { createExpense } from "../travelAPI";

const ExpenseForm = ({ assignedUserId, refresh,travelId }) => {
  const [amount, setAmount] = useState("");
  const [category,setCategory] = useState("");
  const [expenseDate, setExpenseDate] = useState("");
  const [file, setFile] = useState(null);

  const submit = async () => {
    const form = new FormData();
    form.append("amount", amount);
    form.append("category", category);
    form.append("expenseDate", expenseDate);
    form.append("assignedId",assignedUserId);
    form.append("file", file);


    await createExpense(travelId, form);

    setAmount("");
    setCategory("");
    setExpenseDate("")
    setFile(null);
    refresh();
  };

  return (
    <div className="bg-white p-6 rounded shadow mb-6">
      <h3 className="font-semibold mb-4">Add Expense</h3>

      <div className="flex flex-col gap-4">

        <TextField
          label="Category"
          value={category}
          onChange={e => setCategory(e.target.value)}
        />
        <TextField
          label="Amount"
          value={amount}
          onChange={e => setAmount(e.target.value)}
        />

        <TextField
         type="date"
          value={expenseDate}
          onChange={e => setExpenseDate(e.target.value)}
        />

        <input
          type="file"
          onChange={e => setFile(e.target.files[0])}
        />

        <Button variant="contained" onClick={submit}>
          Submit Expense
        </Button>
      </div>
    </div>
  );
};

export default ExpenseForm;