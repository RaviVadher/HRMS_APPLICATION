import { useEffect, useState } from "react";
import { fetchGame, createGame, addInterest, fetchDashboard } from "../gameAPI";
import toast from "react-hot-toast";
import DashboardLayout from "../../layout/DashboardLayout";
import { useAuth } from "../../context/AuthContext";
import { CircularProgress, Button } from "@mui/material";
import { useNavigate } from "react-router-dom";

export default function GameList() {

    const [games, setGames] = useState([]);
    const [dashboard, setDashboard] = useState([]);
    const [load, setLoad] = useState(false);
    const { user, loading } = useAuth();
    const [showForm, setShowForm] = useState(false);
    const [gameName, setGameName] = useState("");

    const navigate = useNavigate();

    useEffect(() => {
        loadGame();
        loadDashboard();
        const id = setInterval(loadDashboard, 30000);
        return () => clearInterval(id);

    }, []);

    const loadGame = async () => {
        setLoad(true);
        try {
            const res = await fetchGame();
            setGames(res);
        } catch {
            toast.error("Failed to load games");
        }
        setLoad(false);
    };

    const loadDashboard = async () => {
        try {
            const data = await fetchDashboard();
            console.log(data);
            setDashboard(data);
        } catch { }
    };

    const handleCreate = async () => {
        if (!gameName.trim())
            return toast.error("Enter game name");

        try {
            await createGame(gameName);
            setGameName("");
            setShowForm(false);
            toast.success("Game created");
            loadGame();
        } catch {
            toast.error("Failed to create game");
        }
    };

    const handleInterest = async (gameId) => {
            try{
            await addInterest(gameId);
            toast.success("Interest added");
            }catch(e){
                console.log(e)
            };
    
    };

    const slotsByGame = dashboard.reduce((acc, slot) => {
        if (!acc[slot.game]) acc[slot.game] = [];
        acc[slot.game].push(slot);
        return acc;
    }, {});


    if (loading || load)
        return (
            <DashboardLayout>
                <div className="flex justify-center mt-20">
                    <CircularProgress />
                </div>
            </DashboardLayout>
        );

    return (
        <DashboardLayout>
            <div className="p-6">
                <div className="flex items-center justify-between w-full mb-8">
                    <h1 className="text-2xl font-semibold">
                        Games
                    </h1>
                    {user?.role === "ROLE_Hr" && (
                        <div>
                            <Button
                                variant="contained"
                                onClick={() => setShowForm(!showForm)}
                            >
                                + Create Game
                            </Button>
                            {showForm && (
                                <div className="border p-4 rounded mt-3 w-64 bg-white shadow">
                                    <input
                                        className="border p-2 w-full mb-3 rounded"
                                        placeholder="Game name"
                                        value={gameName}
                                        onChange={(e) => setGameName(e.target.value)} />
                                    <Button
                                        fullWidth
                                        variant="contained"
                                        onClick={handleCreate}>
                                        Save
                                    </Button>
                                </div>)}
                        </div>
                    )}
                </div>

                <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-5">
                    {games.map(g => (
                        <div
                            key={g.gameId}
                            className="border rounded-lg p-4 shadow-sm hover:shadow transition">
                            <h2 className="text-xl font-bold mb-4 text-center">
                                {g.gameName}
                            </h2>
                            <div className="flex flex-col gap-3">
                                <button
                                    className="bg-blue-400 text-white py-2 rounded hover:bg-blue-400"
                                    onClick={() => handleInterest(g.gameId)}
                                >
                                    Add Interest
                                </button>
                                <button
                                    className="bg-blue-500 text-white py-2 rounded hover:bg-blue-400"
                                    onClick={() => navigate(`/games/${g.gameId}/slot`)}
                                >
                                    Book Game
                                </button>
                                <button
                                    className="bg-blue-500 text-white py-2 rounded hover:bg-blue-400"
                                    onClick={() => navigate(`/game/${g.gameId}/bookingHistory/${user.id}`)} >
                                    History
                                </button>
                                {user?.role === "ROLE_Hr" && (
                                    <button
                                        className="bg-blue-500 text-white py-2 rounded hover:bg-blue-400"
                                        onClick={() => navigate(`/games/${g.gameId}/gameconfig`)} >
                                        Config Game
                                    </button>
                                )}
                            </div>
                            {slotsByGame[g.gameName]?.length > 0 && (
                                <div className="mt-5 border-t pt-3">
                                    <h3 className="font-semibold mb-2 text-gray-700">
                                        Upcoming Slots
                                    </h3>
                                    {slotsByGame[g.gameName].map((s, i) => (
                                        <div
                                            key={i}
                                            className="bg-gray-50 border rounded p-2 mb-2 text-sm"  >
                                            <div className="font-medium">
                                                {s.start} - {s.end}
                                            </div>
                                            <div className="text-gray-600">
                                                {s.players.join(", ")}
                                            </div>
                                        </div>
                                    ))}
                                </div>)}
                        </div>))}
                </div>
            </div>
        </DashboardLayout>
    );  
}