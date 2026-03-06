import { useState, useEffect } from "react";
import { useParams } from "react-router-dom"
import { getJobById } from "../jobAPI";
import DashboardLayout from "../../layout/DashboardLayout";
import { Button, Tabs, Tab, CircularProgress } from "@mui/material";
import ReferTable from "../componants/ReferTable";
import ReferForm from "../componants/ReferForm";
import ShareForm from "../componants/ShareForm";
import ShareTable from "../componants/ShareTable";
import { useAuth } from "../../context/AuthContext";
import { updateJobStatus, getRefers, getShares } from "../jobAPI";

export default function JobDetails() {
    const { id } = useParams();
    const { user, loading } = useAuth();
    const [job, setJob] = useState(null);
    const [tab, setTab] = useState(0);
    const [shareOpen, setShareOpen] = useState(false);
    const [referOpen, setReferOpen] = useState(false);
    const [reload, setReload] = useState(true);
    const [status, setStatus] = useState("Open")
    const [refers, setReferes] = useState([]);

    //job details
    useEffect(() => {
        load();
    }, [id, status]);

    const load = async () => {
        setReload(true);
        const data = await getJobById(id);
        console.log(data);
        setJob(data);
        setReload(false);
    };

    const handleUpdate = async (id) => {
        await updateJobStatus(id, { status: status })
        console.log(status)
        await load();
    };

    //refer details
    useEffect(() => {
        getRefer(id);
    }, [id,loading,user]);

    const getRefer = async (id) => {
        if(loading) return;
        const data = await getRefers(id,user.role);
        console.log(data);
        setReferes(data);
    }

    //share deatils
    const [shares, setShares] = useState([]);

    useEffect(() => {
        getShare(id)
    }, [id,loading,user]);

    const getShare = async (id) => {
        if(loading) return;
        const data = await getShares(id,user.role);
        console.log(data);
        setShares(data);
    }

    if (reload || loading) return <DashboardLayout><CircularProgress></CircularProgress></DashboardLayout>
    return (
        <DashboardLayout>
            <div className="max-w-5xl mx-auto bg-white p-6 rounded-lg shadow">
                <h3 className="text-2xl font-semibold mb-4">Title: {job.title}</h3>
                <p><strong>Created By: </strong> {job.createdBy}</p>
                <p><strong>HR Email: </strong>  {job.hrMail}</p>
                <p><strong>Status: </strong>{job.status}</p>

                {user.role === "ROLE_Hr" && job.status === "Open" && (
                    <div className="flex items-center gap-3 mb-4" >
                        <select className="border rounded px-2 py-1" onChange={(ev) => setStatus(ev.target.value)} >
                            <option value={"Open"}>Open</option>
                            <option value={"Closed"}>Closed</option>
                        </select>
                        <button className="bg-blue-600 text-white px-3 py-1 rounded" onClick={() => handleUpdate(job.jobId)}>
                            Save
                        </button>
                    </div>
                )}

                <p className="text-gray-600 mb-6">{job.summary}</p>
                    {job.status === "Open" && (
                        <div className="flex gap-3 mb-6">
                            <Button variant="outlined" onClick={() => setShareOpen(true)}>
                                Share
                            </Button>
                            <Button variant="contained" onClick={() => setReferOpen(true)}>
                                Refer
                            </Button> </div>
                        )}
               
             
                        <Tabs value={tab} onChange={(e, v) => setTab(v)}>
                            <Tab label="Referrals" />
                            <Tab label="Shared" />
                        </Tabs>
                        {tab === 0 ? <ReferTable jobId={job.jobId} refers={refers} /> : <ShareTable jobId={job.jobId} shares={shares} />}
              
                <ShareForm open={shareOpen} close={() => setShareOpen(false)} job={job} getShare={getShare} />
                <ReferForm open={referOpen} close={() => setReferOpen(false)} job={job} getRefer={getRefer} />
            </div>
        </DashboardLayout>

    );
}