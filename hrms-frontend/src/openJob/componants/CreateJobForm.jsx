import { useState, useEffect } from "react";
import {
  TextField,
  Button,
  MenuItem,
  CircularProgress, Modal, Box, Select
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import { getUsers, createJob } from "../jobAPI";
import toast from "react-hot-toast";

const CreateJobForm = () => {

  const [form, setForm] = useState({
    title: "",
    summary: "",
    reviewerIds: [],
    file: ""
  });
  const [file, setFile] = useState(null);
  const [reviewers, setReviewers] = useState([]);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const res = await getUsers();
      setReviewers(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleReviewerChange = (e) => {
    setForm({ ...form, reviewerIds: e.target.value });
  };

  const handleFile = (e) => {
    setFile(e.target.files[0]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      if(!form.title.trim()) return toast.error("Title is required");
      if(form.reviewerIds.length===0) return toast.error("Title is required");
      if(!file) return toast.error("upload Jd file");

      const data = new FormData();
      data.append("title", form.title);
      data.append("summary", form.summary);
      data.append("file", file);
      form.reviewerIds.forEach(r => {
        data.append("reviewerIds", r)
      });

      await createJob(data);
      toast.success("Job Created Successfully");
      setForm({
        title: "",
        summary: "",
        reviewerIds: [],
      });
      navigate("/jobs");
    }
    finally {
      setLoading(false);
    }
  };

  return (
    <Modal open={open} onClose={close}>
      <Box className="bg-white p-6 rounded w-[400px] mx-auto mt-40 space-y-4">
        <h2 className="text-2xl font-semibold mb-6">
          Create Job
        </h2>
        <form onSubmit={handleSubmit} className="space-y-5">
          <TextField
            label="Job Title"
            name="title"
            value={form.title}
            onChange={handleChange}
            fullWidth
            required
          />
          <TextField
            label="Summary"
            name="summary"
            value={form.summary}
            onChange={handleChange}
            fullWidth
            multiline
            rows={3}
            required
          />


          <Select multiple value={form.reviewerIds} fullWidth label="Select Reviewers" onChange={handleReviewerChange}>
            {reviewers.map((r) => (
              <MenuItem key={r.userId} value={r.userId}>
                {r.username}
              </MenuItem>
            ))}
          </Select>

          <div>
            <label className="block mb-1 font-medium">
              Upload JD
            </label>
            <input type="file" accept="application/pdf" onChange={handleFile} />
          </div>

          <Button
            variant="contained"
            type="submit"
            fullWidth
            disabled={loading}
          >
            {loading ? <CircularProgress size={24} /> : "Create Job"}
          </Button>
        </form>

      </Box>
    </Modal>
  );
}

export default CreateJobForm;