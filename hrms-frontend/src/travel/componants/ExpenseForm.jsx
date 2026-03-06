import { useState,useRef } from "react";
import { TextField, Button, CircularProgress } from "@mui/material";
import { createExpense } from "../travelAPI";
import toast from "react-hot-toast";

const ExpenseForm = ({ assignedId, refresh,travelId,total }) => {
  const [amount, setAmount] = useState("");
  const [category,setCategory] = useState("");
  const [expenseDate, setExpenseDate] = useState("");
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);

  const fileRef = useRef(null);

  const submit = async () => {
   const currentTotal = total[expenseDate] || 0;
   setLoading(true);
   if(!category) return toast.error("category is required");
   if(!amount) return toast.error("amount is required");
   if(!expenseDate) return toast.error("expenseDate is required");
   if (currentTotal + parseFloat(amount) > 2000) {
   toast.error("Daily expense limit of RS.2000 exceeded.");
   return;
   } 

    const form = new FormData();
    form.append("amount", amount);  

    form.append("category", category);
    form.append("expenseDate", expenseDate);
    form.append("assignedId",assignedId);
    form.append("file", file);

    try{
    await createExpense(travelId, form);
    setAmount("");
    setCategory("");
    setExpenseDate("")
    if(fileRef.current){
         fileRef.current.value="";
      }
    refresh();
    toast.success("expense submitted")
    }
    finally
    {
      setLoading(false);
      setFile(null);
    }
  };

  return (
    <div className="bg-white p-6 rounded shadow mb-6">
      <h3 className="font-semibold mb-4">Add Expense</h3>
      <div className="flex flex-col gap-4">

        <TextField
          label="Category"
          value={category}
          onChange={e => setCategory(e.target.value)}
          required/>

        <TextField
          label="Amount"
          type="number" 
          value={amount}
          onChange={e => setAmount(e.target.value)}
          required/>

        <TextField
         type="date"
          value={expenseDate}
          onChange={e => setExpenseDate(e.target.value)}
          required />

        <input
          type="file"
          ref={fileRef}
          onChange={e => setFile(e.target.files[0])}/>
       
        <Button variant="contained" onClick={submit}>
          {loading ? (<CircularProgress/>):( "Submit Expense")}     
        </Button>
      </div>
    </div>
  );
};

export default ExpenseForm;