import React, { use, useEffect, useState } from "react";
import { Badge, IconButton, Popover, List, ListItem, ListItemText, Typography, Divider } from "@mui/material";
import NotificationsIcon from "@mui/icons-material/Notifications";
import CloseIcon from "@mui/icons-material/Close";
import api from "../api/axios";

const Notification = ({ userId }) => {
    const [anchorEl, setAnchorEl] = useState(null);
    const [notifications, setNotifications] = useState([]);

    const open = Boolean(anchorEl);

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
        fetchNotifications();
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    useEffect(()=>{
        const interval = setInterval(()=>{
            fetchNotifications();
        },5000);

        return ()=> clearInterval(interval);
    })

    const fetchNotifications = async () => {
        const res = await api.get(`/notification/${userId}`);
        setNotifications(res.data);

    };

    const markAsRead = async (id) => {
        try {
            await api.put(`/notification/read/${id}`);
            setNotifications(notifications.filter(n => n.id !== id));
        } catch (err) {
            console.error(err);
        }
    };

    useEffect(() => {
        fetchNotifications();
    }, []);

    return (
        <>
            <IconButton color="inherit" onClick={handleClick}>
                <Badge badgeContent={notifications.length} color="error">
                    <NotificationsIcon />
                </Badge>
            </IconButton>

            <Popover
                open={open}
                anchorEl={anchorEl}
                onClose={handleClose}
                anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
                transformOrigin={{ vertical: "top", horizontal: "right" }}>
                <div className="w-80 max-h-96 overflow-y-auto bg-white">
                    <div className="flex justify-between items-center px-4 py-2 font-semibold text-gray-800">
                        Notifications
                    </div>
                    <Divider />
                    {notifications.length === 0 ? (
                        <div className="px-4 py-3 text-sm text-gray-500">
                            No new notifications
                        </div>
                    ) : (
                        <List>
                            {notifications.map((notification) => (
                                <ListItem key={notification.id}
                                    secondaryAction={
                                        <IconButton
                                            edge="end"
                                            onClick={() => markAsRead(notification.id)}>
                                            <CloseIcon fontSize="small" />
                                        </IconButton> }>
                                    <ListItemText
                                        primary={notification.msg}
                                        secondary={new Date(notification.createdAt).toLocaleString()}/>
                                </ListItem>
                            ))}
                        </List>
                    )}
                </div>
            </Popover>
        </>
    );
};

export default Notification;