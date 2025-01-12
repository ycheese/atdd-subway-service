package nextstep.subway.line.application;

import nextstep.subway.exception.EntityNotFoundException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest request) {
        Station upStation = stationService.stationById(request.getUpStationId());
        Station downStation = stationService.stationById(request.getDownStationId());
        Distance distance = new Distance(request.getDistance());
        Line persistLine = lineRepository.save(new Line(request.getName(), request.getColor(), upStation, downStation, distance, request.getSurcharge()));
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLines() {
        List<Line> persistLines = lineRepository.findAll();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("노선이 존재하지 않습니다.", id));
    }


    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = lineRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("노선이 존재하지 않습니다.", id));
        persistLine.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor(), lineUpdateRequest.getSurcharge()));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.stationById(request.getUpStationId());
        Station downStation = stationService.stationById(request.getDownStationId());
        Distance distance = new Distance(request.getDistance());

        line.addSection(upStation, downStation, distance);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.stationById(stationId);
        line.removeLineStation(station);
    }
}

