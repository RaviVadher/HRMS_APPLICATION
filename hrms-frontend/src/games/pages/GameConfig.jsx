import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { TextField, Button, CircularProgress } from "@mui/material";
import DashboardLayout from "../../layout/DashboardLayout";
import { getConfig, createConfig, editConfig } from "../gameAPI";

export default function GameConfig() {
    const { id } = useParams();
    console.log(id);
    const navigate = useNavigate();

    const [form, setForm] = useState({
        startTime: "",
        endTime: "",
        slotDuration: "",
        capacity: "",
    });

    const [loading, setLoading] = useState(true);
    const [editMode, setEditMode] = useState(false);
    const [exists, setExists] = useState(false);

    const handleChange = (key, value) => {
        setForm((prev) => ({ ...prev, [key]: value }));
    };

    useEffect(() => {
        setLoading(true);
        getConfig(id).then((data) => {
            if (data) {
                setExists(true);
                setForm({
                    startTime: data.startTime,
                    endTime: data.endTime,
                    slotDuration: data.slotDuration,
                    capacity: data.capacity,
                });
            }
            setLoading(false);
        });
    }, [id]);

    const handleSave = async () => {
        setLoading(true);
        if (exists) {
            await editConfig(id, form);
        }
        else {
            await createConfig(id, form);
        }
        setExists(true);
        setEditMode(false);
        setLoading(false);
    };

    if (loading) return <DashboardLayout><CircularProgress/></DashboardLayout>;
    return (
        <DashboardLayout>
            <div className="p-6 max-w-md">
                <h1 className="text-xl font-semibold mb-4">Game Configuration</h1>

                <div className="flex flex-col gap-4">
                    {editMode ? (
                        <>
                            <TextField
                                label="Start Time"
                                type="time"
                                value={form.startTime}
                                onChange={(e) => handleChange("startTime", e.target.value)}
                            />

                            <TextField
                                label="End Time"
                                type="time"
                                value={form.endTime}
                                onChange={(e) => handleChange("endTime", e.target.value)}
                            />

                            <TextField
                                label="Slot Duration"
                                type="number"
                                value={form.slotDuration}
                                onChange={(e) => handleChange("slotDuration", e.target.value)}
                            />

                            <TextField
                                label="Capacity"
                                type="number"
                                value={form.capacity}
                                onChange={(e) => handleChange("capacity", e.target.value)}
                            />

                            <Button variant="contained" onClick={handleSave}>
                                Save
                            </Button>
                        </>
                    ) : exists ? (
                        <>
                            <div>Start Time: {form.startTime}</div>
                            <div>End Time: {form.endTime}</div>
                            <div>Slot Duration: {form.slotDuration}</div>
                            <div>Capacity: {form.capacity}</div>

                            <Button variant="contained" onClick={() => setEditMode(true)}>
                                Edit Config
                            </Button>

                        </>
                    ) : (
                        <Button variant="contained" onClick={() => setEditMode(true)}>
                            Create Config
                        </Button>
                    )}

                    <Button onClick={() => navigate(-1)}>Back</Button>
                </div>
            </div>
        </DashboardLayout>
    );
}