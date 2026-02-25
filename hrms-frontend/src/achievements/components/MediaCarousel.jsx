import React, { useEffect, useState } from "react";
import { previewMedia } from "../achievementsAPI";

const MediaCarousel = ({ media, postId }) => {
  const [current, setCurrent] = useState(0);
  const [mediaUrls, setMediaUrls] = useState([]);

  useEffect(() => {
    const loadMedia = async () => {
      const urls = [];

      for (const item of media) {
        try {
          const resp = await previewMedia(postId, item.mediaId);
          const blobUrl = URL.createObjectURL(resp.data);
          urls.push({
            ...item,
            previewUrl: blobUrl,
          });
        } catch (err) {
          console.error("Media preview failed", err);
        }
      }

      setMediaUrls(urls);
    };

    if (media?.length) {
      loadMedia();
    }

    return () => {
      mediaUrls.forEach((m) =>
        URL.revokeObjectURL(m.previewUrl)
      );
    };
  }, [media, postId]);

  if (!mediaUrls.length) return null;

  const item = mediaUrls[current];

  return (
    <div className="relative w-full rounded-xl overflow-hidden bg-black">

      {item.mediaType === "VIDEO" ? (
        <video
          src={item.previewUrl}
          controls
          className="w-full max-h-[500px] object-contain"
        />
      ) : (
        <img
          src={item.previewUrl}
          alt=""
          className="w-full max-h-[500px] object-contain"
        />
      )}

      {mediaUrls.length > 1 && (
        <>
          <button
            onClick={() =>
              setCurrent((prev) =>
                prev === 0 ? mediaUrls.length - 1 : prev - 1
              )
            }
            className="absolute left-4 top-1/2 -translate-y-1/2 bg-white/80 px-3 py-1 rounded-full"
          >
            ‹
          </button>

          <button
            onClick={() =>
              setCurrent((prev) =>
                prev === mediaUrls.length - 1 ? 0 : prev + 1
              )
            }
            className="absolute right-4 top-1/2 -translate-y-1/2 bg-white/80 px-3 py-1 rounded-full"
          >
            ›
          </button>
        </>
      )}
    </div>
  );
};

export default MediaCarousel;