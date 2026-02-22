import { useEffect, useState } from "react";
import { fetchGame, createGame } from "../gameAPI";
import toast from "react-hot-toast";
import DashboardLayout from "../../layout/DashboardLayout";
import { useAuth } from "../../context/AuthContext";
import { CircularProgress, Button } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { addInterest } from "../gameAPI";

export default function GameList() {
    const [games, setGames] = useState([]);
    const [load, setLoad] = useState(false);
    const { user, loading } = useAuth();
    const [showForm, setShowForm] = useState(false);
    const [gameName, setGameName] = useState("")
    const navigate = useNavigate();

    useEffect(() => {
        loadGame();
    }, []);

    const handleCreate = async () => {
        await createGame(gameName);
        const upadated = await fetchGame();
        setGames(upadated);
        setGameName("")
        setShowForm(false);
        toast.success("game created successfully");
    }

    const loadGame = async () => {
        setLoad(true);
        const res = await fetchGame();
        setGames(res);
        setLoad(false);
    }

    const handleInterest = async (gameId) => {
        await addInterest(gameId);
        toast.success("Interest added successfully");
    }

    if (loading || load) return (<DashboardLayout> <CircularProgress /> </DashboardLayout>)

    return (
        <DashboardLayout>

            <div className="p-6">
                <div className="flex items-center justify-between w-full mb-8 ">
                    <h1 className="text-2xl font-semibold mb-10">Games</h1>

                    {user?.role === "ROLE_Hr" && (
                        <>
                            <Button className="" variant="contained" onClick={() => setShowForm(!showForm)}>
                                + Create Game
                            </Button>

                            {showForm && (
                                <div className="border p-4 rounded mb-6 max-w-sm">
                                    <input className="border p-2 w-full mb-3 " type="text"
                                        placeholder="Game name"
                                        value={gameName}
                                        onChange={(e) => setGameName(e.target.value)}
                                    />
                                    <Button onClick={handleCreate} className="w-full bg-blue-300 text-white">save</Button>

                                </div>
                            )}
                        </>
                    )}
                </div>
                <div className="grid md:grid-cols-2 gap-5">
                    {games.map(g => (
                        <div key={g.gameId} className="border rounded-lg p-4">

                            <h2 className="text-2xl font-bold text-gray-900 mb-2 text-center">{g.gameName}</h2>

                            <div className="flex flex-col gap-3">
                                <button className="w-  bg-blue-400 text-white py-2.5 px-5 rounded-lg font-semibold hover:bg-blue-300 transition duration-150" onClick={() => handleInterest(g.gameId)}>
                                    Add Interest
                                </button>
                                <button className="w-full  bg-blue-400 text-white py-2.5 px-5 rounded-lg font-semibold hover:bg-blue-300 transition duration-150" onClick={() => navigate(`/games/${g.gameId}/slot`)}>
                                    Book Game
                                </button>

                                <button className="w-full  bg-blue-400 text-white py-2.5 px-5 rounded-lg font-semibold hover:bg-blue-300 transition duration-150" onClick={() => navigate(`/game/${g.gameId}/bookingHistory/${user.id}`)}>
                                    History
                                </button>
                                {user?.role === "ROLE_Hr" && (
                                    <button className="w-full bg-blue-400 text-white py-2.5 px-5 rounded-lg font-semibold hover:bg-blue-300 transition duration-150" onClick={() => navigate(`/games/${g.gameId}/gameconfig`)}>
                                        config Game
                                    </button>
                                )}
                            </div>
                        </div>))}
                </div>
            </div>
        </DashboardLayout>
    )
};

