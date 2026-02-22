import toast from "react-hot-toast";
import api from "../api/axios";

//get games 
export const fetchGame=async ()=>{
    
    const res = await api.get("/games/getAll")
    return res.data;
}

//create game
export const createGame = async(gameName)=>
{
    const res = await api.post("/games/create",gameName);
}

//get config
export const getConfig = async(gameId)=>{

   console.log(gameId);
    const res = await api.get(`/games/${gameId}/gameConfig`)
    console.log(res.data);
    return res.data;
}

//edit config
export const editConfig = async(gameId,form)=>{
    const res = api.patch(`/games/${gameId}/gameConfig`,form)
    return res.data;
}

//create config
export const createConfig = async(gameId,form)=>{

    const res =await api.post(`/games/${gameId}/gameConfig`,form)
    return res.data;
}

//get slots
export const getSlot = async(gameId)=>{
    const res =await api.get(`/slot/${gameId}`);
    return res.data;
}

//get interested players
export const getPlayers = async(gameId)=>
{
     const res =await api.get(`/games/${gameId}/gameInterest`);
     return res.data;
}

//book slote
export const bookSlot = async(data)=>
{
     const res =  await api.post("/slot/booking",data);
     return res.data;
}

//get maximum capacity of game
export const getMax = async(gameId)=>{

     const res = await api.get(`/games/${gameId}/gameConfig`)
    return res.data.capacity;
}

//get user history
export const getHistory = async(gameId,userId)=>{
     const res = await api.get(`/slot/game/${gameId}/${userId}/getHistory`)
     return res.data;
}

//cancel booking
export const cancelBooking = async(bookedId)=>
{
     const res = await api.patch(`/slot/${bookedId}/cancel`);
     return res.data;
}

//add interest
export const addInterest = async(gameId)=>
{
        const res = await api.post(`/games/${gameId}/gameInterest`);
        return res.data;
}