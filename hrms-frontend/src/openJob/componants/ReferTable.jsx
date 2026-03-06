import { useEffect, useState } from "react";
import { getRefers, getCvUrl } from "../jobAPI";

export default function ReferTable({ refers }) {



  const openDocument = async (referId) => {
    const res = await getCvUrl(referId);
    console.log(res.headers["content-type"])
    const blob = new Blob([res.data], { type: res.headers["content-type"] });
    const url = window.URL.createObjectURL(blob);
    window.open(url);
  };


  return (
    <>
      <h2 className="text-2xl font-semibold mt-3">Refered Job List</h2>
      <div className="flex justify-between mb-6">
        <table className="w-full">
          <thead className="bg-gray-100">
            <tr>
              <th>ReferID</th>
              <th className="p-3">ReferBy</th>
              <th>ReferDate</th>
              <th>FriendName</th>
              <th>ReferredEmail</th>
              <th>CV</th>
            </tr>
          </thead>

          <tbody>

            {refers.length === 0 ? (
              <tr><td colSpan="8" className="text-center p-4">No Referred Found</td></tr>
            ) : (
            refers.map((t) => (

              <tr key={t.referId} className="border-t text-center">
                <td>{t.referId}</td>
                <td>{t.sharedBy}</td>
                <td>{t.sharedDate.slice(0, 19)}</td>
                <td>{t.friendName}</td>
                <td className="p-3">{t.friendEmail}</td>
                <td>
                  <button
                    className="text-blue-600"
                    onClick={() => openDocument(t.referId)}>
                    View
                  </button>
                </td>
              </tr>)
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
}
