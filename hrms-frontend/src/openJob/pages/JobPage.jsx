import { useEffect, useState } from "react";
import DashboardLayout from "../../layout/DashboardLayout";
import { Button, TextField, MenuItem, CircularProgress } from "@mui/material";
import { getJob } from "../jobAPI";
import { useAuth } from "../../context/AuthContext";
import JobTable from "../componants/JobTable";
import { useNavigate } from "react-router-dom";

export default function JobPage() {
    const { user, loading } = useAuth();
    const [jobs, setJobs] = useState([]);
    const [filter, setFilter] = useState("Open");
    const navigate = useNavigate();

    useEffect(() => {
        load()
    }, []);

    const load = async () => {
        const data = await getJob();
        console.log(data);
        setJobs(data);
    };

    const filtered = filter === "ALL" ? jobs : jobs.filter(j => j.status === filter)

    if (loading) return <DashboardLayout><CircularProgress size={24} /></DashboardLayout>
    return (
        <DashboardLayout>
            <div className="flex justify-between mb-6">
                <h2 className="text-2xl font-semibold">Jobs</h2>
                {user.role === "ROLE_Hr" && <TextField select size="small" value={filter} onChange={e => setFilter(e.target.value)}>
                    <MenuItem value="ALL">All</MenuItem>
                    <MenuItem value="Open">Open</MenuItem>
                    <MenuItem value="Closed">Closed</MenuItem>
                </TextField>}
                {user.role === "ROLE_Hr" &&
                    (<Button variant="contained" onClick={() => navigate("/job/createForm")}>
                        + Create Job
                    </Button>)}
            </div>
            <JobTable jobs={filtered} />
        </DashboardLayout>

    );
}

