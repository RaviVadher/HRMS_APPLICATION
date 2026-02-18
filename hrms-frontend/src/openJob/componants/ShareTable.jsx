import { useEffect, useState } from "react";
import { getShares } from "../jobAPI";

export default function ShareTable({ jobId }) {
  const [shares, setShares] = useState([]);

  useEffect(() => {
    getShare(jobId)
  }, [jobId]);

  const getShare = async (jobId) => {
    const data = await getShares(jobId);
    console.log(data);
    setShares(data);
  }

  return (
    <>
      <h2 className="text-2xl font-semibold">Shared Job List</h2>

      <div className="flex justify-between mb-6">
        <table className="w-full">
          <thead className="bg-gray-100">
            <tr>
              <th>SharedID</th>
              <th className="p-3">SharedBy</th>
              <th>SharedDate</th>
              <th>Shared Email</th>
            </tr>
          </thead>

          <tbody>
            {shares.map((t) => (
              <tr key={t.jobId} className="border-t text-center">
                <td>{t.jobId}</td>
                <td className="p-3">{t.sharedBy}</td>
                <td>{t.sharedDate}</td>
                <td>{t.sharedEmail}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
}