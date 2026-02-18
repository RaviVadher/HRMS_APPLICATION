import { useEffect, useState } from "react";
import { getRefers, getCvUrl } from "../jobAPI";

export default function ReferTable({ jobId }) {

  const [refers, setReferes] = useState([]);

  useEffect(() => {
    getRefer(jobId);
  }, [jobId]);

  const getRefer = async (jobId) => {
    const data = await getRefers(jobId);
    console.log(data);
    setReferes(data);
  }

  const openDocument = async (referId) => {
    const res = await getCvUrl(referId);
    console.log(res.headers["content-type"])
    const blob = new Blob([res.data], { type: res.headers["content-type"] });
    const url = window.URL.createObjectURL(blob);
    window.open(url);
  };


  return (
    <>
      <h2 className="text-2xl font-semibold">Refered Job List</h2>
      <div className="flex justify-between mb-6">
        <table className="w-full">
          <thead className="bg-gray-100">
            <tr>
              <th>ReferID</th>
              <th className="p-3">ReferBy</th>
              <th>ReferDate</th>
              <th>ReferdName</th>
              <th>ReferedEmail</th>
              <th>CV</th>
            </tr>
          </thead>

          <tbody>
            {refers.map((t) => (

              <tr key={t.referId} className="border-t text-center">
                <td>{t.referId}</td>
                <td>{t.sharedBy}</td>
                <td>{t.sharedDate}</td>
                <td>{t.friendName}</td>
                <td className="p-3">{t.friendEmail}</td>
                <td>
                  <button
                    className="text-blue-600"
                    onClick={() => openDocument(t.referId)}>
                    View
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
}
