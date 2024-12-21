import { useQuery } from "@tanstack/react-query";
import { QUERY_KEY } from "../utils/queriesKey";
import { getListSuggestionPeople } from "../service/suggestion";

const useGetListSuggestion = () => {
  return useQuery({
    queryKey: QUERY_KEY.fetchListPeopleSuggestion(),
    queryFn: getListSuggestionPeople,
  });
};
export { useGetListSuggestion };
