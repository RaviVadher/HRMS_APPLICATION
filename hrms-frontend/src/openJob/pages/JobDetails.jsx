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
import { updateJobStatus, getRefers,getShares } from "../jobAPI";

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
    }, [id]);

    const getRefer = async (id) => {
        const data = await getRefers(id);
        console.log(data);
        setReferes(data);
    }

    //share deatils
    const [shares, setShares] = useState([]);

    useEffect(() => {
        getShare(id)
    }, [id]);

    const getShare = async (id) => {
        const data = await getShares(id);
        console.log(data);
        setShares(data);
    }

    if (reload || loading) return <DashboardLayout><CircularProgress></CircularProgress></DashboardLayout>
    return (
        <DashboardLayout>
            <div className="bg-white p-5 rounded shadow">
                <h3 className="text-xl font-semibold">Title:{job.title}</h3>
                <span className="text "><b>Created By:</b> {job.createdBy}</span> <br />
                <span className="text "><b>Email:</b>   {job.hrMail}</span><br />
                <span className="text "><b>Status: </b>{job.status}</span>

                {user.role === "ROLE_Hr" && job.status === "Open" && (
                    <div className="space-x-2" >
                        <select className="border p-1" onChange={(ev) => setStatus(ev.target.value)} >
                            {/* <option>{job.status}</option> */}
                            <option value={"Open"}>Open</option>
                            <option value={"Closed"}>Closed</option>
                        </select>
                        <button onClick={() => handleUpdate(job.jobId)}>
                            Save
                        </button>
                    </div>
                )}

                <p className="text-gray-600 mb-3">{job.summary}</p>
                <div className="flex gap-3 mb-4">

                    {job.status === "Open" && (
                        <>
                            <Button variant="outlined" onClick={() => setShareOpen(true)}>
                                Share
                            </Button>

                            <Button variant="contained" onClick={() => setReferOpen(true)}>
                                Refer
                            </Button></>)}
                </div>
                {user.role === "ROLE_Hr" && (
                    <>
                        <Tabs value={tab} onChange={(e, v) => setTab(v)}>
                            <Tab label="Referrals" />
                            <Tab label="Shared" />
                        </Tabs>
                        {tab === 0 ? <ReferTable jobId={job.jobId} refers={refers} /> : <ShareTable jobId={job.jobId} shares={shares} />}
                    </>
                )}
                <ShareForm open={shareOpen} close={() => setShareOpen(false)} job={job} getShare={getShare}/>
                <ReferForm open={referOpen} close={() => setReferOpen(false)} job={job} getRefer={getRefer} />
            </div>
        </DashboardLayout>

    );
}